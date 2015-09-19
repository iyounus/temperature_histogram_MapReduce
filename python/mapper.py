#!/usr/bin/env python
from __future__ import division
import sys


def set_hist(hmin, hmax, nbins):
    bin_width = (hmax - hmin)/nbins
    bins = []
    for i in range(nbins+1):
        bins.append(float(hmin + i*bin_width))
    return bins


def main():
    quality_code = set([0, 1, 4, 5, 9])
    bins = set_hist(-50., 50., 20)

    for input in sys.stdin.readlines():
        airTemperature = float(input[87:92])/10.
        quality = int(input[92:93])

        if airTemperature < 999.9 and quality in quality_code:
            bin_id = -9
            for i in range(len(bins)):
                if airTemperature >= bins[i] and airTemperature < bins[i+1]:
                    bin_id = i + 1  # following ROOT convension
                    break

            print str(bin_id) + '\t1'

if __name__ == "__main__":
    main()
