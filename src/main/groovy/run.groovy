import autoru.Form
import groovy.sql.Sql
import groovyx.net.http.HTTPBuilder

def sql = Sql.newInstance("jdbc:h2:d:/Dropbox/cars/autoru.h2/db", "su", "su")
def offersTable = sql.dataSet('offers')

def getTrs(String url) {
    for (int i = 0;i < 100;i ++) {
        try {
            def http = new HTTPBuilder(url)
            def html = http.get([:])
            return html.'**'.findAll {it.@class.toString()=='list' && it.name() == 'TABLE'}[0].'TBODY'[0].'TR'
        } catch (RuntimeException e) {
            e.printStackTrace()
        }
    }
    throw new IllegalStateException("can't access $url")
}

int n = 0

for (def mark: Form.options['mark_id']) {

    println mark.key
    for (int i = 0;; i ++) {

        def url = new Form(params: [
                'mark_id': mark.value, 'used_key': '5', 'custom_key': '1', 'available_key': '1', '_p': "${i+1}"
        ]).url

        if (i > 200) {
            break
        }

        println "parsing page ${i+1}..."

        def now = new Date()
        def trs = getTrs(url)
        if (trs.size() <= 1) {
            break
        }
        def entries = trs[1..-1].collect {
            [
                    'mark': mark.key,
                    'model': it.'TD'[0].toString().trim(),
                    'url': it.'TD'[0].'A'[0].@href.toString().trim(),
                    'price': it.'TD'[1].toString().trim().replace(' ','') as int,
                    'year': it.'TD'[2].toString().trim() as int,
                    'engine': it.'TD'[3].toString().trim(),
                    'type': it.'TD'[4].toString().trim(),
                    'right_hand': it.'TD'[4].'DIV'[0].'IMG'.findAll {it.'@alt'.toString() == 'Правый руль'}.size() > 0,
                    'running': it.'TD'[5].toString().trim().replace(' ','') as int,
                    'photo': it.'TD'[6].'IMG'.size() > 0,
                    'color': it.'TD'[8].'DIV'[0].@title.toString().trim(),
                    'city': it.'TD'[9].toString().trim(),
                    'availability': it.'TD'[11].toString().trim(),
                    'parsed_at': now
            ]
        }
        entries.each {
            offersTable.add(it)
            n ++
        }
        println "$n offers parsed"
        if (entries.empty) {
            break
        }
    }
}

sql.close()