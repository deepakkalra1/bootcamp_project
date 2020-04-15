//package com.e_commerce_project_ttn.E_Commerce_Project_TTN;
//
//import ParentProductCategory;
//import Product;
//import Category;
//import Role;
//import AddressPojo;
//import com.e_commerce_project_ttn.E_Commerce_Project_TTN.model.DAO.Seller.*;
//import Seller;
//import ProductRepository;
//import Seller_Repository;
//import SellerRepository;
//import SellerUserRole;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.LockUserOnOverAttempt;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
////all dao testing here
//
//@SpringBootTest
//class ECommerceProjectTtnApplicationTests {
//
//	@Autowired
//	SellerRepository customer_repository;
//	@Autowired
//	Seller_Repository seller_repository;
//	@Autowired
//	ProductRepository product_repository;
//
//
//
//
//	//working perfect....Seller and AddressPojo Dao (OneToMany)
//	@LockUserOnOverAttempt
//	void TestingForUserAndAddress() {
//		AddressPojo address1 = new AddressPojo();
//		address1.setAddress_line("rohini");
//		AddressPojo address2 = new AddressPojo();
//		address2.setAddress_line("vikaspuri");
//
//
//
//		List<AddressPojo> addresses = new ArrayList<AddressPojo>();
//		addresses.add(address1);
//		addresses.add(address2);
//
//
//		Seller user = new Seller();
//		user.setFirst_name("gunjan");
//		user.setEmail("deepak@Gmail.com");
//		user.setPassword("12345678");
//		user.setLast_name("kalra");
//		user.setIs_active(true);
//		user.setAddressList(addresses);
//		user.setContact("9818124789");
//		address1.setSeller(user);
//		address2.setSeller(user);
//
//		customer_repository.save(user);
//	}
//
//
//
//
//	//working perfect....User_Seller and Address_seller Dao
//	@LockUserOnOverAttempt
//	void TestingForSellerUserAndAddress() {
//		Address_seller address1 = new Address_seller();
//		address1.setAddress_line("jankpuri");
//		Address_seller address2 = new Address_seller();
//		address2.setAddress_line("dwarka");
//
//		Seller_Block seller_block = new Seller_Block();
//		seller_block.setCompany_name("on_tweak");
//		seller_block.setGst("12345");
//
//
//
//		List<Address_seller> addresses = new ArrayList<Address_seller>();
//		addresses.add(address1);
//		addresses.add(address2);
//
//
//		User_Seller user = new User_Seller();
//		user.setFirst_name("faizal");
//		user.setEmail("amar@Gmail.com");
//		user.setPassword("12345678");
//		user.setLast_name("ali");
//		user.setIs_active(true);
//		user.setAddressList(addresses);
//		user.setContact("9818124789");
//		address1.setSeller_seller(user);
//		address2.setSeller_seller(user);
//
//		seller_block.setSeller_seller(user);
//		user.setSeller_block(seller_block);
//
//		seller_repository.save(user);
//	}
//
//
//
//	/*
//	working properly
//
//
//	Seller -> user_role,address
//
//	address-> user
//
//	user_role -> role,user
//	 */
//
//	@LockUserOnOverAttempt
//	public void test1() throws JsonProcessingException {
//
//		AddressPojo address1 = new AddressPojo();
//		address1.setAddress_line("badli");
//		AddressPojo address2 = new AddressPojo();
//		address2.setAddress_line("ajmer");
//
//
//
//		List<AddressPojo> addresses = new ArrayList<AddressPojo>();
//		addresses.add(address1);
//		addresses.add(address2);
//
//
//		Seller user = new Seller();
//		user.setFirst_name("viju");
//		user.setEmail("deepak@Gmail.com");
//		user.setPassword("12345678");
//		user.setLast_name("kalra");
//		user.setIs_active(true);
//		user.setAddressList(addresses);
//		user.setContact("9818124789");
//		address1.setSeller(user);
//		address2.setSeller(user);
//
//		Role role= customer_repository.findPKofRoleForUser_role();
//
//
//		SellerUserRole user_role = new SellerUserRole();
//		user_role.setRole(role);
//		user_role.setSeller(user);
//		user.setUser_role(user_role);
//
//
//
//
//		customer_repository.save(user);
//
//
//	}
//
//
//
//
//
//
///*
//
//seller -> seller-block,address
//
// */
//
//
//
//	@LockUserOnOverAttempt
//	public void test2(){
//
//		Address_seller address1 = new Address_seller();
//		address1.setAddress_line("punjab");
//		Address_seller address2 = new Address_seller();
//		address2.setAddress_line("haryana");
//
//
//
//		List<Address_seller> addresses = new ArrayList<Address_seller>();
//		addresses.add(address1);
//		addresses.add(address2);
//
//
//
//		User_Seller user = new User_Seller();
//		user.setFirst_name("piyush");
//		user.setEmail("deepak@Gmail.com");
//		user.setPassword("12345678");
//		user.setLast_name("kalra");
//		user.setIs_active(true);
//		user.setAddressList(addresses);
//		user.setContact("9818124789");
//		address1.setSeller_seller(user);
//		address2.setSeller_seller(user);
////		Seller_Block seller_block = new Seller_Block();
////		seller_block.setCompany_name("TTN");
////		seller_block.setSeller_seller(user);
////		user.setSeller_block(seller_block);
//
//
//
//		Role role= seller_repository.findPKofRoleForUser_role();
//
//
//		Seller_Role seller_role = new Seller_Role();
//		seller_role.setRole(role);
//		seller_role.setSeller_seller(user);
//		user.setSeller_role(seller_role);
//		seller_repository.save(user);
//
//
//	}
//
//
//
//	/*
//	product -> seller,subcategory
//
//	 */
//
//
//	@LockUserOnOverAttempt
//	public void test3(){
//
//
//
//		Address_seller address1 = new Address_seller();
//		address1.setAddress_line("punjab");
//		Address_seller address2 = new Address_seller();
//		address2.setAddress_line("haryana");
//
//
//
//		List<Address_seller> addresses = new ArrayList<Address_seller>();
//		addresses.add(address1);
//		addresses.add(address2);
//
//
//
//		User_Seller user = new User_Seller();
//		user.setFirst_name("changu");
//		user.setEmail("deepak@Gmail.com");
//		user.setPassword("12345678");
//		user.setLast_name("kalra");
//		user.setIs_active(true);
//		user.setAddressList(addresses);
//		user.setContact("9818124789");
//		address1.setSeller_seller(user);
//
//
//		address2.setSeller_seller(user);
//		Seller_Block seller_block = new Seller_Block();
//		seller_block.setCompany_name("TTN");
//		seller_block.setSeller_seller(user);
//		user.setSeller_block(seller_block);
//
//
//
//		Role role= seller_repository.findPKofRoleForUser_role();
//
//
//		Seller_Role seller_role = new Seller_Role();
//		seller_role.setRole(role);
//		seller_role.setSeller_seller(user);
//		user.setSeller_role(seller_role);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//		Product product = new Product();
//		product.setName("smart 54inch TV");
//		product.setBrand("TVS");
//		product.setIs_cancellable(true);
//		product.setIs_returnable(true);
//		product.setDescription("the is the new TV");
//		product.setSeller_seller(user);
//
//		Category product_subcategory = new Category();
//		product_subcategory.setName("TV");
//
//		ParentProductCategory parent_product_category = new ParentProductCategory();
//		parent_product_category.setName("Electronics");
//
//		product_subcategory.setParent_product_category(parent_product_category);
//
//		product.setProduct_subcategory(product_subcategory);
//
//		product_repository.save(product);
//
//
//	}
//
//
//}
