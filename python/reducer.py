#!/usr/bin/env python
import sys


def main():
    bin_count = 0
    bin_id = 0

    for input in sys.stdin.readlines():
        input = input.strip().split('\t')
        new_bin_id = int(input[0])

        if bin_id == 0:
            bin_id = new_bin_id

        if bin_id != new_bin_id:
            print str(bin_id) + '\t' + str(bin_count)
            bin_id = new_bin_id
            bin_count = 0

        bin_count += int(input[1])

    if bin_id != 0:
        print str(bin_id) + '\t' + str(bin_count)

if __name__ == "__main__":
    main()
