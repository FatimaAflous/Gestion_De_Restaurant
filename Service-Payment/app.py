from flask import Flask, jsonify
from py_eureka_client.eureka_client import EurekaClient

app = Flask(__name__)

# Configuration de Eureka Client
from app.config.eureka_config import eureka_client

# Endpoint Health Check
@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({"status": "UP"}), 200

from app.services.rabbitmq_consumer import start_consuming
if __name__ == '__main__':
    start_consuming()
    app.run(debug=True)
