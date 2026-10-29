package model;

import java.io.Serializable;

public record BankInfo(String name, String branch) implements Serializable {}

