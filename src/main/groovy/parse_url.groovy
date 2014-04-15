

def url = System.in.withReader {
    it.readLine()
}
def (String path, String params) = url.trim().split("\\?", 2)
println path

def paramMap = [:]
params.split("&").each {
    def (name, value) = it.split("=", 2).collect { URLDecoder.decode(it, "utf8") }
    paramMap[name] = value
}

println "[${paramMap.collect {"'$it.key': '$it.value'"}.join(', ')}]"