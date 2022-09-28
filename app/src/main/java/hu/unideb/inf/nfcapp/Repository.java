package hu.unideb.inf.nfcapp;

import android.widget.Toast;

public class Repository {
    public enum CommunicatorTypeEnum
    {
        MsSqlServer,
        Oracle,
        MySql,
        Xml,
        TextFile
    }

    public CommunicatorTypeEnum CommunicatorType = null;
    public Communicator Communicator = null;
    public User LoggedInUser = null;

    public Repository(CommunicatorTypeEnum enumType){
        this.CommunicatorType = enumType;

        if(enumType.equals(CommunicatorTypeEnum.MsSqlServer)){
            this.Communicator = new SqlDatabaseCommunicator();
        }
        else {
            this.Communicator = null;
        }

        this.LoggedInUser = new User();
    }


}
