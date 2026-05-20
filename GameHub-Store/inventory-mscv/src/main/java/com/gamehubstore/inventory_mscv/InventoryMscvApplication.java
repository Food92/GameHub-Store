package com.gamehubstore.inventory_mscv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients(basePackages = "com.gamehubstore.inventory_mscv.client")
public class InventoryMscvApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryMscvApplication.class, args);
	}

}
