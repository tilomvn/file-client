package com.treasuredata.fileclient.components;

public interface IClientFileComponent {

    boolean connect();

    boolean closeConnect();

    String sendAction(String command);
}
