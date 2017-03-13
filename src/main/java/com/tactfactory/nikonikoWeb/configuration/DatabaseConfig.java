package com.tactfactory.nikonikoWeb.configuration;

import java.util.Properties;

import javax.sql.DataSource;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class DatabaseConfig {

	@Autowired
	private Environment env;

	@Autowired
	private DataSource dataSource;

	@Bean
	public DataSource datasource() {
		DriverManagerDataSource datasource = new DriverManagerDataSource();
		datasource.setDriverClassName(env.getProperty("db.driver"));
		datasource.setUrl(env.getProperty("db.url"));
		datasource.setUsername(env.getProperty("db.username"));
		datasource.setPassword(env.getProperty("db.password"));
		return datasource;

	}

	@Autowired
	private LocalContainerEntityManagerFactoryBean entityManagerFactory;

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory () {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactory.setDataSource(dataSource);

		entityManagerFactory.setPackagesToScan(env.getProperty("entitymanager.packagesToScan"));

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

		Properties additionalProperties = new Properties();
		additionalProperties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
		additionalProperties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		additionalProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		entityManagerFactory.setJpaProperties(additionalProperties);

		return entityManagerFactory;
	}

	@Bean
	public JpaTransactionManager transactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
}
