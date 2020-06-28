package com.gongva.retromvvm.repository;

import com.gongva.retromvvm.repository.base.remote.GeneralRemoteDS;
import com.gongva.retromvvm.repository.login.LoginRemoteDS;

/**
 * Enables injection of production implementations for
 * data source at compile time.
 */
public class Injection {

    public static GeneralRemoteDS provideGeneralRemoteDataSource() {
        return GeneralRemoteDS.getInstance();
    }

    public static LoginRemoteDS provideLoginRemoteDataSource() {
        return LoginRemoteDS.getInstance();
    }
}
