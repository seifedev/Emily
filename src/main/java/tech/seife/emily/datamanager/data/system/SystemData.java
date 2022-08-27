package tech.seife.emily.datamanager.data.system;

public interface SystemData {

    String getMusicChannel();

    void setMusicChannel(String id);

    String getDmChannel();

    void setDmChannel(String id);

    String getCommandPrefix();

    String getOwner();

    void setOwner(String id);

    String getToken();

    void setNewPrefix(String newPrefix);

    void changeSelfDestruct();

    boolean getSelfDestructValue();

    long getSelfDestructDelay();

    void setSelfDestructDelay(long amount);

}
