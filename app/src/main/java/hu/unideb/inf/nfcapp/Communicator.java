package hu.unideb.inf.nfcapp;

import java.sql.Connection;

public interface Communicator {

    public User loginUser();
    public Connection getConnection();
}
