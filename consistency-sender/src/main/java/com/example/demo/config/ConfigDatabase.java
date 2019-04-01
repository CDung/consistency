package com.example.demo.config;


import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;


import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
public class ConfigDatabase {	
	private static SessionFactory instance;
	
	private ConfigDatabase() {
		  Configuration configuration = new Configuration();
//		  configuration.addAnnotatedClass(PaymentDatabaseServerInfo.class);
		  configuration.setProperty("hibernate.connection.driver_class",
			      "org.mariadb.jdbc.Driver");
			  configuration.setProperty("hibernate.connection.url",
			      "jdbc:mariadb://db_container:3306/config?createDatabaseIfNotExist=true");
			  configuration.setProperty("hibernate.connection.username", "root");
			  configuration.setProperty("hibernate.connection.password", "misasme");
			  configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
			  configuration.setProperty("hibernate.hbm2ddl.auto","update");
			  configuration.setProperty("hibernate.show_sql", "false");
			  configuration.setProperty("hibernate.connection.pool_size", "100");
			  configuration.setProperty("hibernate.connection.characterEncoding", "utf8");
			  configuration.setProperty("hibernate.connection.useUnicode", "true");
			  configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");
			  configuration.setProperty("hibernate.search.default.directory_provider", "filesystem");
			  configuration.setProperty("hibernate.search.default.indexBase", "/data/index/default");
		  StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
		      .applySettings(configuration.getProperties());
		  instance= configuration.buildSessionFactory(builder.build());		 
	}
	
	public static SessionFactory getInstance() {
		if (instance==null) new ConfigDatabase();
		return instance;
	}
}
