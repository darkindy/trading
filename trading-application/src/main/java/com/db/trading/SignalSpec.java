package com.db.trading;

import lombok.Data;

import java.util.List;

@Data
public class SignalSpec {
    int id;
    List<String> actions;
}
