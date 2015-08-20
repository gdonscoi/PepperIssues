package net.caiena.github.model.bean;

import java.util.ArrayList;

public class Issue implements IEntidade{

    public String title;

    public String id;

    public String state;

    public String body;

    public ArrayList<Label> labels;

    public Milestone milestone;

}
