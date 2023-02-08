# phonewords script

A small [babashka](https://babashka.org/) script for generating
[phonewords](https://en.wikipedia.org/wiki/Phoneword) for given (phone) numbers.

## Usage

To generate all combinations that contain any (common) English words:

``` shell
echo 43556 | bb -o phonewords.bb
(HELLM HELLN HELLO)
```

To generate combinations that contain exact words:

``` shell
echo 43556 | bb -o phonewords.bb -s
(HELLO)
```

Generate all combinations from numbers in a file:

``` shell
echo "43556\n74663" > numbers.txt
cat numbers.txt | bb -o phonewords.bb -s
(HELLO)
(PHONE)
```

The script can take a specific EDN dictionary file:

``` shell
echo 937286 | bb -o phonewords.bb -d special-dictionary.edn
(ZDRAVO)
```

## License

Copyright (c) 2023 Dejan Josifović, the paranoid times

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software
is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
