package com.projet_restaurant.servicecommandes.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // Méthode pour envoyer une notification de changement de statut de commande
    public void notifyPaymentStatus(Long orderId, String status) {
        String destination = "/topic/order-status/" + orderId;
        simpMessagingTemplate.convertAndSend(destination, status);
        System.out.println("Notification envoyée au frontend pour la commande " + orderId + " avec le statut " + status);
    }
}
