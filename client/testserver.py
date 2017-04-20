#!/usr/bin/python
# -*- coding: utf-8 -*-

# Den här koden är tagen direkt från internet och används i testsyfte.

from http.server import BaseHTTPRequestHandler, HTTPServer
from http.server import SimpleHTTPRequestHandler


# HTTPRequestHandler class

class testHTTPServer_RequestHandler(BaseHTTPRequestHandler):

    # GET

    def do_GET(self):

        # Send response status code

        self.send_response(200)

        # Send headers

        self.send_header('Content-type', 'text/html')
        self.end_headers()

        # Send message back to client

        message = 'Hello world!'

        # Write content as utf-8 data

        self.wfile.write(bytes(message, 'utf8'))
        return

    def end_headers(self):
        self.send_header('Access-Control-Allow-Origin', '*')
        SimpleHTTPRequestHandler.end_headers(self)


def run():
    print('starting server...')

    # Server settings
    # Choose port 8080, for port 80, which is normally used for a http server, you need root access

    server_address = ('127.0.0.1', 8080)
    httpd = HTTPServer(server_address, testHTTPServer_RequestHandler)
    print('running server...')
    httpd.serve_forever()


run()
