const search = () => {
    let query = $("#search-input").val();
    if (query == '') {
        $(".search-result").hide()
    }
    else {
        const url = 'http://localhost:8080/search/' + query;
        var baseUrl = '/user/show-contact/';

        fetch(url).then(response => {
            return response.json();

        }).then((data) => {
            let text = "<div class='list-group'>"
            data.forEach(contact => {
                text += '<a href="/user/' + contact.cId + '/show-contact-profile" class="list-group-item list-group-action">' + contact.name + '</a>';
            });
            text += '</div>';

            $(".search-result").html(text);
            $(".search-result").show();.

        });
        $(".search-result").show()
    }
}


//razor gatway payment
//function for order create  request to server

const paymentStart = () => {
    let amount = $('#amount_field').val();
    if (amount == '' || amount == null) {
        swal("Required", "Amount is Required", "warning");

        return;
    }

    // we will use ajax to send request to server to create order -jquery\

    $.ajax({
        url: '/user/create_order',
        data: JSON.stringify({ amount: amount, info: 'order_request' }),
            contentType: 'application/json',
            type: 'POST',
            dataType: 'json',
        success: function (response) {
            //success
            console.log(response.status);

            if (response.status == "created") {
                //open payment form
                let options = {
                    key: "rzp_test_ADEnLyqI9oALQY",
                    amount: response.amount,
                    currency: "INR",
                    name: "Smart Contact Manager",
                    description:
                        "our donation will directly impact the lives of impoverished children, providing them with vital support and opportunities they deserve. With your generosity, we can:",
                    image:
                        "https://www.savethechildren.in/wp-content/uploads/2022/08/Artboard-2-1.webp",
                    order_id: response.id,
                    handler: function (response) {
                        console.log(response.razorpay_payment_id);
                        console.log(response.razorpay_order_id);
                        console.log(response.razorpay_signature)
                        updatePaymentOnServer(response.razorpay_payment_id, response.razorpay_order_id, "paid");
                        //console.log("payment Succefull")

                    },
                    prefill: {
                        name: "",
                        email: "",
                        contact: ""
                    },
                    notes: {
                        address: "At Post Smart Contact Manager"
                    },
                    theme: {
                        color: "#3399cc"
                    }

                };
                let razorpay = new Razorpay(options);
                razorpay.on('payment.failed', function (response) {
                    console.log(response.error.code);
                    console.log(response.error.description);
                    console.log(response.error.source);
                    console.log(response.error.step);
                    console.log(response.error.reason);
                    console.log(response.error.metadata.order_id);
                    console.log(response.error.metadata.payment_id);
                    // window.alert("oops!! payment failed");
                    swal("Failed!!", "Opps!!!payment failed", "error");
                });
                razorpay.open();

            }



        },
        error: function (erro) {
            //error
            console.log(erro)
            alert("something went wrong !!!")
        }

    })


};


function updatePaymentOnServer(paymentId, orderId, isSuccess) {

    $.ajax({
        url: '/user/update_order',
        data: JSON.stringify({ payment_id: paymentId, order_id: orderId, status: isSuccess }),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response) {
            swal("Congratulation", "payment Succefull", "success");
        },
        error: function (error) {
            swal("Failed", "payment Successfull,but we did not get on server,we will contact you as soon as possible", "warning");
        }
    })
}




//paytm gatway system

async function doPayment() {
    const amount = $('#amount').val();

    const baseUrl = "http://localhost:8081/payment";
    const response = await fetch("http://localhost:8080/payment/start-payment", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }, body: JSON.stringify({ 'amount': amount })
    });

    response.json().then(data => {
        intiateClientModule(data);
        console.log(data);


    }).catch(error => {
        console.log(error)
        alert("error intiative")
    })
}




function intiateClientModule(data) {
    const scriptTag = document.createElement("script");
    scriptTag.src = 'https://securegw-stage.paytm.in/merchantpgpui/checkoutjs/merchants/RHLBnF07039627707073.js';
    scriptTag.crossOrigin = 'onScriptLoad();';
    scriptTag.onload = () => {
        var config = {
            "root": "",
            "flow": "DEFAULT",
            "data": {
                "orderId": data.orderId, /* update order id */
                "token": data.body.txnToken, /* update token value */
                "tokenType": "TXN_TOKEN",
                "amount": data.amount /* update amount */
            },
            "merchant": {
                mid: "RHLBnF07039627707073",
                redirect:false
            },
            "handler": {
                "notifyMerchant": function (eventName, data) {
                    console.log("notifyMerchant handler function called");
                    console.log("eventName => ", eventName);
                    console.log("data => ", data);
                },
                "transcationStatus":function(data){
                    console.log("transcation completed")
                    console.log(data);
                    if(data.STATUS="TXN_FAILURE"){
                        swal(data.RESPMSG, "warning");
                    }
                    else if(data.STATUS=='TXN_SUCCESS'){
                        swal(data.RESPMSG,"success");
                    }
                    else{
                        swal("Something Went Wrong", "warning");
                    }
                    window.Paytm.CheckoutJS.close();
                }
            }
        };
        if (window.Paytm && window.Paytm.CheckoutJS) {
            window.Paytm.CheckoutJS.onLoad(function excecuteAfterCompleteLoad() {
                // initialze configuration using init method
                window.Paytm.CheckoutJS.init(config).then(function onSuccess() {
                    // after successfully updating configuration, invoke JS Checkout
                    window.Paytm.CheckoutJS.invoke();
                }).catch(function onError(error) {
                    console.log("error => ", error);
                });
            });
        }
    }
    document.body.appendChild(scriptTag);


}