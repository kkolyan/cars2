import groovyx.net.http.HTTPBuilder

String url = "http://cars.auto.ru/extsearch/cars/used/?mode=extsearch"
def http = new HTTPBuilder(url)
def html = http.get([:])
html.'**'