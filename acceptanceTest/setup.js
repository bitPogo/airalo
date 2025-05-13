function getFromApi(url) {
   return JSON.parse(http.get(url, {
       headers: {
           'Content-Type': 'application/json',
           'Accept-Language': 'en-DE'
       },
   }).body)
}

const countries = getFromApi('https://www.airalo.com/api/v2/countries')
output.countries = countries.map(country => country.title)

const ids = countries.filter(country => country.title === 'Germany' || country.title === 'Taiwan').map(country => country.id)
output.CountryPackages = {}
ids.forEach(id => {
    const countryPackages = getFromApi('https://www.airalo.com/api/v2/countries/' + id)
    output.CountryPackages[countryPackages.slug] = countryPackages.packages
})