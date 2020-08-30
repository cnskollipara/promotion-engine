package com.cart.promotion.mapper;

import com.cart.promotion.beans.ComboPromo;
import com.cart.promotion.beans.Promotion;
import com.cart.promotion.beans.QuantPromo;
import com.cart.promotion.beans.Sku;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SkuMapper {

    String skuDataPath = "sku-price.json";
    String quantPromoDataPath = "quant-promos.json";
    String comboPromoDataPath = "combo-promos.json";
    List<Sku> skus = new ArrayList<>();
    private static Map<Sku, Promotion> skuPromoMap = new HashMap<>();
    private static Map<String, Sku> skuMap = new HashMap<>();

    @Autowired
    public SkuMapper() throws IOException {
        loadSkus();
        loadQuantPromos();
        loadComboPromos();
    }

    private void loadComboPromos() throws IOException {
        File file = isFileAvailable(comboPromoDataPath);
        if (file != null) {
            FileInputStream fis = new FileInputStream(file);
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
            reader.beginArray();
            while (reader.hasNext()) {
                Map map = gson.fromJson(reader, Map.class);
                List<String> skus = (List<String>) map.get("comboSkus");
                int offerPrice = (int) Math.round((Double) map.get("offerPrice"));
                Set<Sku> skuSet = skus.stream().map(s -> skuMap.get(s)).collect(Collectors.toSet());
                ComboPromo promo = new ComboPromo(skuSet, offerPrice);
                skuSet.forEach(sku -> skuPromoMap.put(sku, promo));
            }
            reader.endArray();
            reader.close();
        }
    }

    private void loadQuantPromos() throws IOException {
        File file = isFileAvailable(quantPromoDataPath);
        if (file != null) {
            FileInputStream fis = new FileInputStream(file);
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
            reader.beginArray();
            while (reader.hasNext()) {
                Map map = gson.fromJson(reader, Map.class);
                Sku sku = skuMap.get(map.get("sku"));
                int buys = (int) Math.round((Double) map.get("buys"));
                int offerPrice = (int) Math.round((Double) map.get("offerPrice"));
                QuantPromo promo = new QuantPromo(buys, offerPrice);
                skuPromoMap.put(sku, promo);
            }
            reader.endArray();
            reader.close();
        }
    }

    private void loadSkus() throws IOException {
        File file = isFileAvailable(skuDataPath);
        if (file != null) {
            FileInputStream fis = new FileInputStream(file);
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
            reader.beginArray();
            while (reader.hasNext()) {
                Sku obj = gson.fromJson(reader, Sku.class);
                skus.add(obj);
                skuMap.put(obj.getId(), obj);
            }
            reader.endArray();
            reader.close();
        }
    }

    private File isFileAvailable(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file = new File(ClassLoader.getSystemResource(path).getFile());
            } catch (NullPointerException e) {
                log.error("File ({}) not found. This is fatal.", path);
                file = null;
            }
        }
        return file;
    }

    public Sku getSku(String s) {
        return skuMap.get(s);
    }

    public Promotion getPromo(Sku sku) {
        return skuPromoMap.get(sku);
    }
}
