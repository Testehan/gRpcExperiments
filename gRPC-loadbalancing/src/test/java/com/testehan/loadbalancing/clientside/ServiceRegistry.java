package com.testehan.loadbalancing.clientside;

import io.grpc.EquivalentAddressGroup;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// user for client side load balancing
public class ServiceRegistry {

    private static final Map<String, List<EquivalentAddressGroup>> MAP = new HashMap<>();

    //a call to this method will have parameters like : "payment-service" and 127.0.0.1:8080,128.0.0.1:8080
    public static void register(String service, List<String> instances){
        List<EquivalentAddressGroup> addressGroupList = instances.stream()
                .map(i -> i.split(":"))
                .map(a -> new InetSocketAddress(a[0], Integer.parseInt(a[1])))
                .map(EquivalentAddressGroup::new)
                .collect(Collectors.toList());
        MAP.put(service, addressGroupList);
    }

    public static List<EquivalentAddressGroup> getInstances(String service){
        return MAP.get(service);
    }

}
