package com.example.david.tabatatimer.data;

import java.util.List;

/**
 * Created by David on 31/10/2016.
 */

public class TabataconfigDAO {
    public static List<Tabataconfig> selectAll() {return Tabataconfig.listAll(Tabataconfig.class);}
}
