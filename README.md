# Table of Contents
1. [How To Execute](README.md#how-to-execute)
2. [Implementation Summary](README.md#implementation-summary)

# How To Execute

The code is implemented in Java. You need to have JDK 8 ready to run the code.

The source code doesn't depend on any third party libraries and could directly be compile and executed using the command line in `run.sh`. This would be enough to generate the result files.

The unit test code is under `./test` and is depending on junit library. The jar files are already put under `./lib` so executing `./run_unit_test` would verify if all unit tests pass or not. 

# Implementation Summary

There are three sub-problems in this coding challenge:

- How to detect if the donor is a repeat donor
- Given a repeat donor, how could we find all qualified contributions to the particular recipient from all repeat donors in a given zip code and year
- Given all qualified contributions how could we get the percentile amount. 

Since the input record is provided in a stream way, we need to maintain some hash map or hash set to solve the problems above. Based on the definition of repeat donor, we need to create a map from donor to the year so the contribution in a later year could be identified to mark the donor as repeat. 

In terms of how to find all qualified contributions, my solution is to store input contributions in hierarchy. Top level key is recipient so all contributions to the same recipient are group in one model. Second level breaks down the contributions to non-repeat donors and repeat donors. Third level is different for non-repeat and repeat donors. For repeat donor the key to the map is year + zip code based on the request in problem description. For non-repeat donor the key is the donor so in the future when a certain donor is marked as repeat, we could easily move those contributions into repeat donor group.

The last problem is how to efficiently get the percentile of all contributions. Since the input is a stream, the size of data keeps changing which makes percentile run a non-trivial question. The naive way is to compute the cardinal rank, sort the list and get access to the rank every time when there is a new request. There is no perfect solution to keep sorted property and random access property access at the same time. My solution is keeping random access and using quick select to find the Kth smallest element, which is faster than quick sort in this case. The average time complexity is O(n).