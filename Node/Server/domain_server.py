from flask import Flask, jsonify

app = Flask(__name__)

servers = {
    1: "http://172.16.198.33:3100",
    2: "http://172.16.192.197:3100",
    3: "http://172.16.196.61:3100",
    4: "http://172.16.193.101:3100",
    5: "http://172.16.193.42:3100",
    6: "http://172.16.194.255:3100",
    7: "http://172.16.196.240:3100",
    8: "http://172.16.192.61:3100",
    9: "http://172.16.193.47:3100",
    10: "http://172.16.192.75:3100",
    11: "http://172.16.196.244:3100",
    12: "http://172.16.192.221:3100",
    13: "http://172.16.194.206:3100",
    14: "http://172.16.199.207:3100",
    15: "http://172.16.197.45:3100",
    16: "http://172.16.199.16:3100",
    17: "http://172.16.196.242:3100",
    18: "http://172.16.193.56:3100"

}

@app.route('/servers', methods=['GET'])
def get_servers():
    server_list = list(servers.values())
    return jsonify(server_list)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)

