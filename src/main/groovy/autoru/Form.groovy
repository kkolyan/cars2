package autoru

class Form {
    static options = [
            'mark_id': [
//                    'Любая': '0', 'Отечественные': '-1', 'Иномарки': '-2',
                    'AC': '1', 'Acura': '5', 'Alfa Romeo': '7', 'Aro': '11',
                    'Asia': '12', 'Aston Martin': '14', 'Audi': '15', 'Austin': '16', 'Beijing': '23', 'Bentley': '25', 'BMW': '30',
                    'BMW Alpina': '8', 'Brilliance': '671', 'Bugatti': '36', 'Buick': '37', 'BYD': '40', 'Byvin': '2838',
                    'Cadillac': '41', 'Changan': '1284', 'ChangFeng': '646', 'Chery': '48', 'Chevrolet': '49', 'Chrysler': '50',
                    'Citroen': '52', 'Dacia': '59', 'Dadi': '60', 'Daewoo': '61', 'Daihatsu': '63', 'Daimler': '64',
                    'De Lorean': '67', 'Derways': '69', 'Dodge': '71', 'DongFeng': '597', 'Doninvest': '72', 'Eagle': '74',
                    'FAW': '77', 'Ferrari': '79', 'Fiat': '80', 'Fisker': '2548', 'Ford': '82', 'Foton': '588', 'FSO': '87',
                    'Geely': '88', 'Geo': '89', 'GMC': '90', 'Great Wall': '95', 'Hafei': '573', 'Haima': '2040', 'Honda': '104',
                    'HuangHai': '847', 'Hummer': '107', 'Hurtan': '1245', 'Hyundai': '109', 'Infiniti': '114',
                    'Iran Khodro': '112', 'Isuzu': '121', 'JAC': '124', 'Jaguar': '125', 'Jeep': '127', 'JMC': '1157', 'Kia': '134',
                    'Koenigsegg': '137', 'KTM': '447', 'Lamborghini': '145', 'Lancia': '146', 'Land Rover': '147',
                    'Landwind': '148', 'Lexus': '152', 'Lifan': '645', 'Lincoln': '153', 'Lotus': '154', 'Luxgen': '2974',
                    'Mahindra': '160', 'Maserati': '164', 'Maybach': '165', 'Mazda': '166', 'Mc Laren': '167',
                    'Mercedes-Benz': '170', 'Mercury': '171', 'Metrocab': '173', 'MG': '174', 'Mini': '177', 'Mitsubishi': '181',
                    'Mitsuoka': '182', 'Morgan': '185', 'Nissan': '191', 'Oldsmobile': '196', 'Opel': '197', 'Pagani': '200',
                    'Peugeot': '205', 'Plymouth': '206', 'Pontiac': '207', 'Porsche': '208', 'Proton': '211', 'PUCH': '212',
                    'Renault': '217', 'Rolls-Royce': '219', 'Rover': '221', 'Saab': '222', 'Saturn': '226', 'Scion': '230',
                    'SEAT': '231', 'ShuangHuan': '670', 'Skoda': '236', 'SMA': '784', 'Smart': '237', 'Spyker': '242',
                    'Ssang Yong': '243', 'Subaru': '246', 'Suzuki': '247', 'TATA': '251', 'Tesla': '1689', 'Tianma': '254',
                    'Tianye': '255', 'Toyota': '260', 'Trabant': '261', 'Volkswagen': '273', 'Volvo': '274', 'Vortex': '534',
                    'Wartburg': '276', 'Westfield': '278', 'Wiesmann': '280', 'Xin Kai': '282', 'ZX': '780', 'Бронто': '2392',
                    'ВАЗ': '288', 'ГАЗ': '292', 'ЗАЗ': '296', 'ЗИЛ': '297', 'ИЖ': '299', 'КамАЗ': '301', 'ЛУАЗ': '311',
                    'Москвич (АЗЛК)': '316', 'СеАЗ': '232', 'СМЗ': '895', 'ТагАЗ': '1038', 'УАЗ': '336', 'Эксклюзив': '1670'
            ],
            // состояние
            'used_key': [
                    '': '', 'Все кроме битых': '5', 'Отличное': '1', 'Хорошее': '2', 'Среднее': '3', 'Требует ремонта': '4'
            ],
            // таможня
            'custom_key': ['': '', 'Растаможен': '1', 'Не растаможен': '2'],
            'country_id': ['Россия': '1'],
            'region[]': ['Санкт-Петербург': '85'],
            'available_key': ['': '', 'В наличии': '1', 'На заказ': '2', 'В пути': '3']
    ]
    static defaults = [
            // Легковые автомобили
            'category_id': '15',
            // поддержаные
            'section_id': '1', 'subscribe_id': '', 'filter_id': '', 'submit': 'Найти',
            'mark_id': '', 'year[1]': '', 'year[2]': '', 'color_id': '', 'price_usd[1]': '', 'price_usd[2]': '',
            'currency_key': 'RUR', 'body_key': '', 'run[1]': '', 'run[2]': '', 'engine_key': '0',
            'engine_volume[1]': '', 'engine_volume[2]': '', 'drive_key': '', 'engine_power[1]': '',
            'engine_power[2]': '', 'transmission_key': '0', 'used_key': '', 'wheel_key': '', 'custom_key': '',
            'available_key': '', 'change_key': '', 'owner_pts': '', 'stime': '0', 'country_id': '1', 'has_photo': '0',
            'region[]': '89', 'region_id': '89', 'sort_by': '2', 'city_id': '', 'output_format': '1', 'client_id': '0',
            'extras[1]': '0', 'extras[2]': '0', 'extras[3]': '0', 'extras[4]': '0', 'extras[5]': '0', 'extras[6]': '0',
            'extras[7]': '', 'extras[8]': '0', 'extras[9]': '0', 'extras[10]': '0', 'extras[11]': '0', 'extras[12]': '',
            'extras[13]': '0', 'extras[14]': '0', 'extras[15]': '0', 'extras[16]': '0', 'extras[17]': '0',
            'extras[18]': '', 'extras[19]': '', 'extras[20]': '', 'extras[21]': '', 'extras[22]': '', 'extras[23]': '0',
            'extras[24]': '0', 'extras[25]': '', 'extras[26]': '', 'extras[27]': '0', 'extras[28]': '0',
            'extras[29]': ''
    ]

    final String urlBase = 'http://cars.auto.ru/list/'

    def params = [:]

    String getUrl() {
        def map = defaults.clone();
        map += params

        "$urlBase?${map.collect {"${URLEncoder.encode(it.key, 'utf8')}=${URLEncoder.encode(it.value, 'utf8')}"}.join('&')}"
    }
}
