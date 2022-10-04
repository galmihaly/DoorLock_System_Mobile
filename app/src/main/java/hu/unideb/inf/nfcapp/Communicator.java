package hu.unideb.inf.nfcapp;

import java.util.List;

public interface Communicator {

    LoginTypeEnum loginUser(String username, String password);
    List<MyLog> getLogbyDate(int year, int mounth, int dayofMounth);
}
