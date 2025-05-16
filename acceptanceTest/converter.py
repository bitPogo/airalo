import xmltodict, json

strings = ''

with open('../offer/src/androidMain/res/values/strings.xml', 'r') as file:
  for line in file:
        strings += line

parsed = xmltodict.parse(strings)

strings  = dict()

for stringPair in parsed['resources']['string']:
    strings[stringPair['@name']] = stringPair['#text']

with open("strings.js", "w") as f:
  f.write("output.Strings = {")
  for key, value in sorted(strings.items()):
      f.write(f'\n\t{key}: "{value}",')
  f.write("}")