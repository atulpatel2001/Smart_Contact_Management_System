package com.smart.contact.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.contact.model.Contact;
import com.smart.contact.model.User;

public interface ContactRepository extends JpaRepository<Contact,Integer>{
	@Query("from Contact as c where c.user.id =:id")
	public Page<Contact> getContactByUserId(@Param("id")int id,Pageable pageable);
	public List<Contact> findByNameContainingAndUser(String name,User user);
	@Query("from Contact as c where c.user.id =:id")
	public List<Contact> getContactByUserId2(@Param("id")int id);



}
