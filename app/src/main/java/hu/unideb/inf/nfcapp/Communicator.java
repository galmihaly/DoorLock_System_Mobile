package hu.unideb.inf.nfcapp;

import java.sql.Connection;

public interface Communicator {

    LoginTypeEnum loginUser(String username, String password);
}
