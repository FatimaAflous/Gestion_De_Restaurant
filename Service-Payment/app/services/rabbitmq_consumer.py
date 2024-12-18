# services/rabbitmq_consumer.py
import sys
import os

# Ajoute le chemin de app au sys.path pour résoudre les imports
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

# Maintenant tu peux importer rabbitmq_config
from config import rabbitmq_config
import pika
from services.payment_service import process_payment

def setup_rabbitmq_connection():
    connection_params = pika.ConnectionParameters(
        host=rabbitmq_config.RABBITMQ_HOST,
        port=rabbitmq_config.RABBITMQ_PORT,
        credentials=pika.PlainCredentials(rabbitmq_config.RABBITMQ_USERNAME, rabbitmq_config.RABBITMQ_PASSWORD),
        virtual_host=rabbitmq_config.RABBITMQ_VIRTUAL_HOST
    )
    return pika.BlockingConnection(connection_params)

def on_message(channel, method_frame, header_frame, body):
    # Traiter le message ici
    print(f"Informations de commande reçues : {body.decode()}")
    # Lancer le processus de paiement avec Stripe ici
      # Appeler le service de paiement
    try:
        process_payment(body.decode())
    except Exception as e:
        print(f"Erreur lors du traitement du paiement : {e}")
    
    # Acknowledge le message après traitement
    channel.basic_ack(delivery_tag=method_frame.delivery_tag)
    

def start_consuming():
    connection = setup_rabbitmq_connection()
    channel = connection.channel()
    channel.queue_declare(queue='commande.queue', durable=True)
    channel.basic_consume(queue='commande.queue', on_message_callback=on_message)
    print('Attente de messages. Pour quitter, appuyez sur CTRL+C')
    channel.start_consuming()

if __name__ == '__main__':
    start_consuming()
