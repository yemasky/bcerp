package core.jdbc.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DbcpPool {
	private final String dirverClassName = "com.mysql.cj.jdbc.Driver";
	private Config config = null;
	private Connection connection;
	private int close_num = 0;
	/**
	 * 数据库连接池（dbcp连接池）对象引用
	 */
	private BasicDataSource dbcpDataSource;
	private HikariDataSource hikariCPdataSource;

	public DbcpPool(Config config) throws SQLException {
		try {
			Class.forName(dirverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new SQLException("找不到驱动类.");
		}
		this.config = config;
		init();
	}

	public Connection getConnection() throws SQLException {
		if(this.connection != null && this.connection.isValid(0)) {
			return this.connection;
		}
		if(this.connection != null) this.connection.close();
		if (this.config.getDrive().equals("HikariCP")) {
			this.connection = this.hikariCPdataSource.getConnection();
			if(this.connection.isValid(0)) {
				return this.connection;
			} else {
				this.connection.close();
				return this.getConnection();
			}
			
		}
		this.connection = this.dbcpDataSource.getConnection();
		return this.connection;
	}

	public int getActiveConnectionNum() {
		if (this.config.getDrive().equals("HikariCP")) {
			return this.hikariCPdataSource.getMinimumIdle();
		}
		return this.dbcpDataSource.getNumActive();
	}

	public void init() {
		//System.out.println("=================>"+this.config.getDrive());
		if (this.config.getDrive().equals("HikariCP")) {
			System.out.println("=================>HikariCP==========>");
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(this.config.getDbDsn() + "&serverTimezone=GMT");
			config.setUsername(this.config.getDbUsername());
			config.setPassword(this.config.getDbPassword());
			// 配置连接池大小
			config.setMaximumPoolSize(this.config.getMaxConnection()); // 最大连接数
			config.setMinimumIdle(5); // 最小空闲连接数
			// 创建数据源
			this.hikariCPdataSource = new HikariDataSource(config);
		} else {
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriverClassName(dirverClassName);
			dataSource.setUsername(this.config.getDbUsername());
			dataSource.setPassword(this.config.getDbPassword());
			dataSource.setUrl(this.config.getDbDsn() + "&serverTimezone=GMT");
			dataSource.setInitialSize(this.config.getMinConnection());// 初始连接数
			dataSource.setMaxTotal(this.config.getMaxConnection());// 最大连接数
			dataSource.setMaxIdle(10);// 最大空闲连接数
			dataSource.setMaxWaitMillis(2000);// 2秒超时
			dataSource.setMinIdle(1);// 最小空闲连接数
			this.dbcpDataSource = dataSource;
		}
	}

	/*public void close(Connection connection) throws SQLException {
		connection.close();
	}*/
	
	public void close(Connection conn) throws SQLException {
		this.close_num++;
		System.out.println("-------=======>关闭链接："+this.close_num);
		this.connection.close();
		conn.close();
		//if (this.config.getDrive().equals("HikariCP")) this.hikariCPdataSource.close();
	}

}
