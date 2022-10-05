package hu.unideb.inf.nfcapp.Databases;

import java.util.List;

import hu.unideb.inf.nfcapp.Enums.LoginTypeEnum;
import hu.unideb.inf.nfcapp.Models.MyLog;

public interface Communicator {

    LoginTypeEnum loginUser(String username, String password);
    List<MyLog> getLogbyDate(int year, int mounth, int dayofMounth);
}
