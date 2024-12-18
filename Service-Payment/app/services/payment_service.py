import stripe
import json
from config.stripe_config import stripe_config
from pymongo import MongoClient
from config import mongodb_config

# Configurer Stripe avec la clé secrète
stripe.api_key = stripe_config["STRIPE_SECRET_KEY"]

def process_payment(message_body):
    """
    Traiter un paiement en utilisant Stripe
    """
    try:
        # Convertir le message reçu (JSON string) en dictionnaire
        commande = json.loads(message_body)

        # Extraire les informations nécessaires
        total = commande.get("total")  # Total de la commande
        client_id = commande.get("clientId")  # Identifiant client
        commande_id = commande.get("id")  # Identifiant commande

        # Créer une demande de paiement (PaymentIntent)
        payment_intent = stripe.PaymentIntent.create(
            amount=int(total * 100),  # Montant en cents
            currency=stripe_config["STRIPE_CURRENCY"],
            description=f"Paiement pour la commande #{commande_id}",
            metadata={
                "commande_id": commande_id,
                "client_id": client_id
            }
        )

        # Log succès
        print(f"Paiement réussi pour la commande ID {commande_id}")
        print(f"Détails du PaymentIntent : {payment_intent}")
          
        # Appeler store_payment pour enregistrer les détails dans MongoDB
        store_payment(payment_intent)
                
        # Retourner le PaymentIntent pour des besoins supplémentaires (logs, notifications, etc.)
        return payment_intent

    except stripe.error.StripeError as e:
        # Gestion des erreurs Stripe
        print(f"Erreur Stripe : {e.user_message}")
        raise e
    except Exception as e:
        # Gestion des autres erreurs
        print(f"Erreur inattendue : {e}")
        raise e

def store_payment(payment_intent):

    try:
        print(f"Connecting to MongoDB with URI: {mongodb_config.MONGODB_URI}")
        client = MongoClient(mongodb_config.MONGODB_URI)
        db = client["payments_db"]

        # Log paiement
        payment_data = {
            "payment_intent_id": payment_intent["id"],
            "amount": payment_intent["amount"],
            "currency": payment_intent["currency"],
            "status": payment_intent["status"],
            "client_id": payment_intent["metadata"]["client_id"],
            "commande_id": payment_intent["metadata"]["commande_id"],
            "created": payment_intent["created"],
        }
        print(f"Storing payment data: {payment_data}")

        result = db.payments.insert_one(payment_data)
        if result.acknowledged:
            print(f"Paiement enregistré avec l'ID : {result.inserted_id}")
        else:
            print("Erreur lors de l'enregistrement du paiement.")
    except Exception as e:
        print(f"Erreur inattendue : {e}")
