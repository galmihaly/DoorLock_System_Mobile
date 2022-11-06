package hu.unideb.inf.nfcapp.Databases;

import hu.unideb.inf.nfcapp.Models.User;

public class Repository {
    public enum CommunicatorTypeEnum
    {
        MsSqlServer,
        Oracle,
        MySql,
        Xml,
        TextFile
    }

    public CommunicatorTypeEnum CommunicatorType;
    public Communicator Communicator;
    public User LoggedInUser;

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
