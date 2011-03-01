COMPUTATIONAL COMPLEXITY


SIMPLE LCP SOLVER

Theoretical:

step 1 - divide pivot row by pivot number

        3n + 1          divisions
                6n + 2          bigint multiplications
                3n + 1          gcds

step 2 - subtract pivot row appropriate number of times from each row

        3n^2 - 2n - 1   multiplications
                6n^2 - 4n - 2   bigint multiplications
                3n^2 - 2n - 1   gcds
        3n^2 - 2n - 1   subtractions
                9n^2 - 6n - 3   bigint multiplications
                3n^2 - 2n - 1   bigint subtractions
                3n^2 - 2n - 1   gcds

        total:
                15n^2 - 10n - 5 bigint multiplications
                6n^2 - 4n - 2   gcds
                3n^2 - 2n - 1   bigint subtractions

step 4 - find new leaving index

        n^2             divisions (max)
                2n^2            bigint multiplications (max)
        n^2             comparisons (max)
                2n^2            bigint multiplications (max)
                n^2             bigint compare (max)


For n = 1378:

step 1
        - 8270 bigint multiplication
        - 4135 gcds

step 2
        - 28469475 bigint multiplications
        - 11387790 gcds
        - 5693895 subtractions

step 4
        - 7595536 multiplications (max)
        - 1898884 compares (max)

        
Observed in practice:



REVISED LCP SOLVER