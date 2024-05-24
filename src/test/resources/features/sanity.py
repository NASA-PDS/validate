#! /usr/bin/env python3

import sys

for fn in sys.argv[1:]:
    print (fn)
    with open(fn,'rt') as file: lines = file.readlines()
    for index,line in enumerate(lines):
        line = line.strip()
        line_test = [line.startswith('|'), line.endswith('|')]
        if line.startswith ('#'): continue
        if any(line_test):
            if not all(line_test):
                print (f'{fn}:{index}: does not start or end with "|"', line)
            if line.count('|') != 10:
                print (f'{fn}:{index}: has wrong number of "|"', line)
            if line.count('"') & 1:
                print (f'{fn}:{index}: contains an odd number of quotes', line)
            for seg in line.split('|'):
                if seg.count('"') & 1:
                    print (f'{fn}:{index}: segement not properly quoted', line)
                    break
