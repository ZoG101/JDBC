package br.com.alura.bytebank;

import java.sql.Connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionFactory {

    public Connection recuperarConexao() {
        
        try {
         
            return createDataSource().getConnection();
            
        } catch (Exception e) {
            
            throw new RuntimeException(e);

        }

    }

    private HikariDataSource createDataSource() {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3307/byte_bank");
        config.setUsername("root");
        config.setPassword("root");
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);

    }
    
}
