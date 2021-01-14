package com.gmail.podkutin.dmitry.votingsystem.to;

import com.gmail.podkutin.dmitry.votingsystem.HasId;

public abstract class BaseTo implements HasId {
    protected Integer id;

    public BaseTo(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
