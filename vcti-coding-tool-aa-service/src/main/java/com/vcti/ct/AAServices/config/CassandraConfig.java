package com.vcti.ct.AAServices.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.vcti.ct.AAServices.model.Permissions;

@Configuration
@EnableCassandraRepositories
public class CassandraConfig extends AbstractCassandraConfiguration implements AAConstants {

	@Value("${spring.data.cassandra.contact-points}")
	private String contactPoints;

	@Value("${spring.data.cassandra.port}")
	private int port;

	@Value("${spring.data.cassandra.keyspace-name}")
	private String keySpace;

	@Value("${spring.data.cassandra.username}")
	private String userName;

	@Value("${spring.data.cassandra.password}")
	private String password;

	@Value("${spring.data.cassandra.user-table}")
	private String userTable;

	@Value("${spring.data.cassandra.role-table}")
	private String roleTable;

	@Value("${spring.data.cassandra.role-permission-mapping-table}")
	private String rolePermMappingTable;

	@Autowired
	Permissions perm;

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	@Override
	protected String getKeyspaceName() {
		return keySpace;
	}

	@Override
	protected String getContactPoints() {
		return contactPoints;
	}

	@Bean
	@Override
	public CassandraCqlClusterFactoryBean cluster() {
		CassandraCqlClusterFactoryBean bean = new CassandraCqlClusterFactoryBean();
		bean.setKeyspaceCreations(getKeyspaceCreations());
		bean.setContactPoints(getContactPoints());
		bean.setUsername(getUserName());
		bean.setPassword(getPassword());
		return bean;
	}

	@Override
	protected int getPort() {
		return port;
	}

	@Override
	public SchemaAction getSchemaAction() {
		return SchemaAction.CREATE_IF_NOT_EXISTS;
	}

	protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
		return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(getKeyspaceName()).ifNotExists());
	}

	@Override
	protected List<String> getStartupScripts() {
		List<String> startupScriptList = new ArrayList<String>();
		// Create Queries
		startupScriptList.addAll(getListOfCreateQuery());
		// Insert Queries
		startupScriptList.addAll(getListOfInsertQuery());
		return startupScriptList;

	}

	private List<String> getListOfCreateQuery() {
		List<String> createQueryList = new ArrayList<String>();
		createQueryList.add("CREATE TABLE IF NOT EXISTS " + getKeyspaceName() + "." + userTable
				+ "(id text, name text, username text PRIMARY KEY, password text, roleid text, experience int)");

		createQueryList.add("CREATE TABLE IF NOT EXISTS " + getKeyspaceName() + "." + roleTable
				+ "(id text PRIMARY KEY, rolename text)");

		createQueryList.add("CREATE TABLE IF NOT EXISTS " + getKeyspaceName() + "." + rolePermMappingTable
				+ "(id text PRIMARY KEY, roleid text, permissionid text)");

		return createQueryList;
	}

	private List<String> getListOfInsertQuery() {

		List<String> insertQueryList = new ArrayList<String>();
		// Role Table
		populateRoleTable(insertQueryList);

		// RolePerMapping Table
		populateRolePerMappingTable(insertQueryList);

		// Add one default admin user
		insertQueryList.add("INSERT INTO " + getKeyspaceName() + "." + userTable
				+ "(id, name, username, password, roleid, experience) VALUES ('1','ADMIN','ADMIN','admin','1',0)");

		return insertQueryList;
	}

	private void populateRoleTable(List<String> insertQueryList) {
		RoleEnum[] roleEnum = AAConstants.RoleEnum.values();
		int index = 1;
		for (RoleEnum role : roleEnum) {
			insertQueryList.add("INSERT INTO " + getKeyspaceName() + "." + roleTable + "(id,rolename) VALUES ('" + index
					+ "','" + role.name() + "')");
			index++;
		}

	}

	private void populateRolePerMappingTable(List<String> insertQueryList) {
		// ADMIN
		int mappingIndex = 1;
		if (perm == null || perm.getPerm() == null || perm.getPerm().length == 0) {
			System.out.println("Permission Properties not loaded properly");
			return;
		}
		System.out.println("Permission Properties loaded successfully");
		for (int permIndex = 0; permIndex < perm.getPerm().length; permIndex++) {
			insertQueryList.add("INSERT INTO " + getKeyspaceName() + "." + rolePermMappingTable
					+ "(id, roleid, permissionid) VALUES ('" + mappingIndex + "','1','" + permIndex + "')");
			mappingIndex++;
		}

		// RECRUITMENT
		insertQueryList.add("INSERT INTO " + getKeyspaceName() + "." + rolePermMappingTable
				+ "(id, roleid, permissionid) VALUES ('" + mappingIndex++ + "','2','1')");
		insertQueryList.add("INSERT INTO " + getKeyspaceName() + "." + rolePermMappingTable
				+ "(id, roleid, permissionid) VALUES ('" + mappingIndex++ + "','2','2')");
		insertQueryList.add("INSERT INTO " + getKeyspaceName() + "." + rolePermMappingTable
				+ "(id, roleid, permissionid) VALUES ('" + mappingIndex++ + "','2','3')");
		insertQueryList.add("INSERT INTO " + getKeyspaceName() + "." + rolePermMappingTable
				+ "(id, roleid, permissionid) VALUES ('" + mappingIndex++ + "','2','4')");
		insertQueryList.add("INSERT INTO " + getKeyspaceName() + "." + rolePermMappingTable
				+ "(id, roleid, permissionid) VALUES ('" + mappingIndex++ + "','2','5')");
		insertQueryList.add("INSERT INTO " + getKeyspaceName() + "." + rolePermMappingTable
				+ "(id, roleid, permissionid) VALUES ('" + mappingIndex++ + "','2','7')");

		// CANDIDATE
		insertQueryList.add("INSERT INTO " + getKeyspaceName() + "." + rolePermMappingTable
				+ "(id, roleid, permissionid) VALUES ('" + mappingIndex++ + "','3','6')");

		// INTERVIEWER
		insertQueryList.add("INSERT INTO " + getKeyspaceName() + "." + rolePermMappingTable
				+ "(id, roleid, permissionid) VALUES ('" + mappingIndex++ + "','4','1')");
		insertQueryList.add("INSERT INTO " + getKeyspaceName() + "." + rolePermMappingTable
				+ "(id, roleid, permissionid) VALUES ('" + mappingIndex++ + "','4','3')");
		insertQueryList.add("INSERT INTO " + getKeyspaceName() + "." + rolePermMappingTable
				+ "(id, roleid, permissionid) VALUES ('" + mappingIndex++ + "','4','5')");
		insertQueryList.add("INSERT INTO " + getKeyspaceName() + "." + rolePermMappingTable
				+ "(id, roleid, permissionid) VALUES ('" + mappingIndex++ + "','4','7')");

		// GUEST

	}

}
