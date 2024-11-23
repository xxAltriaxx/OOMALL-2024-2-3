package cn.edu.xmu.oomall.aftersale.Dao.Solutions;

import java.util.HashMap;
import java.util.Map;

public class AfterSalesSolutionFactory {
    public static Map<String,AfterSalesSolution> solutionMap = new HashMap<String,AfterSalesSolution>();

    public static AfterSalesSolution getSolution(String type){
        return solutionMap.get(type);
    }
    public static void register(String type,AfterSalesSolution solution){
        solutionMap.put(type,solution);
    }
}
