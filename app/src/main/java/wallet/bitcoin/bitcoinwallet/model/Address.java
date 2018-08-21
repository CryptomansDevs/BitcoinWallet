package wallet.bitcoin.bitcoinwallet.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value = "address", unique = true)
})
public class Address {

    public String address;

    public String alias;

    public double savedBallance;

@Generated(hash = 240900771)
public Address(String address, String alias, double savedBallance) {
    this.address = address;
    this.alias = alias;
    this.savedBallance = savedBallance;
}

@Generated(hash = 388317431)
public Address() {
}

public String getAddress() {
    return this.address;
}

public void setAddress(String address) {
    this.address = address;
}

public String getAlias() {
    return this.alias;
}

public void setAlias(String alias) {
    this.alias = alias;
}

public double getSavedBallance() {
    return this.savedBallance;
}

public void setSavedBallance(double savedBallance) {
    this.savedBallance = savedBallance;
}
}
