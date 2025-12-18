package com.localmesh.client;

import com.localmesh.model.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


 //Кэш для хранения данных пользователей на стороне клиента


public class ClientCache {
    private final Map<String, User> userCache = new ConcurrentHashMap<>();
    private final Map<String, String> coordinateCache = new ConcurrentHashMap<>();
    

     //Обновляет информацию о пользователе в кэше

    public void updateUser(User user) {
        if (user != null && user.getId() != null) {
            userCache.put(user.getId(), user);
            System.out.println("[ClientCache] User updated: " + user.getId());
        }
    }
    

     //Получает пользователя из кэша

    public User getUser(String userId) {
        User user = userCache.get(userId);
        if (user == null) {
            System.out.println("[ClientCache] User not found in cache: " + userId);
        }
        return user;
    }
    

     //Удаляет пользователя из кэша

    public void removeUser(String userId) {
        userCache.remove(userId);
        System.out.println("[ClientCache] User removed: " + userId);
    }
    

     //Сохраняет координаты пользователя

    public void saveCoordinates(String userId, String coordinates) {
        coordinateCache.put(userId, coordinates);
        System.out.println("[ClientCache] Coordinates saved for: " + userId);
    }
    

     //Получает координаты пользователя

    public String getCoordinates(String userId) {
        return coordinateCache.get(userId);
    }
    

     //Очищает весь кэш

    public void clear() {
        userCache.clear();
        coordinateCache.clear();
        System.out.println("[ClientCache] Cache cleared");
    }
    

     //Возвращает количество пользователей в кэше

    public int size() {
        return userCache.size();
    }
}
