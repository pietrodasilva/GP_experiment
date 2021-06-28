# ------------------------------------------------------------------------------
#  genetic programming experiment - pietro da silva
# ------------------------------------------------------------------------------

# Boilerplate code
from random import random, choice, randint

# ------------------------------------------------------------------------------
# Field variables used as hyper parameters for the problem:
# 1. 'Maximise the ones'

bits_size = 100
pop_size = 100
n_gen = 500
mutation_rate = 0.0001
tournament_selection_num = 20
pop = []

# ------------------------------------------------------------------------------

# Field variables used as hyper parameters for the problem:
# 2. 'Knapsack problem'

capacity = 104
weights = [25, 35, 45, 5, 25, 3, 2, 2]
values = [350, 400, 450, 20, 70, 8, 5, 5]
opt_solution = [1, 0, 1, 1, 1, 0, 1, 1]
n = len(values)


# ------------------------------------------------------------------------------

def create_pop():
    for i in range(pop_size):
        pop.append("".join(choice('01') for j in range(bits_size)))


def create_pop_knapsack():
    for i in range(0, 7):
        pop.append("".join(choice('01') for j in range(0, 7)))


def determine_fitness_ones_zeroes(chromosome):
    return str(chromosome).count('1')


def determine_fitness_knapsack(chromosome):
    total_value = 0
    total_weight = 0

    fitness = []

    for i in range(len(chromosome)):
        if chromosome[i] == '1':
            total_weight += weights[i]
            total_value += values[i]

        if total_weight <= capacity:
            fitness.append((chromosome[i], total_value))

        else:
            fitness.append((chromosome[i], 0))

        total_value = 0
        total_weight = 0

    return fitness


def average_fitness(population):
    avg_fitness = (determine_fitness_ones_zeroes(population) / pop_size)
    avg_fitness_rounded = round(avg_fitness, 2)
    return avg_fitness_rounded


def show_pop():
    print('-----------------------')
    fitness_avg = 0
    for p in pop:
        print(p + ' ' + str(determine_fitness_ones_zeroes(p)))
        fitness_avg += average_fitness(p)
    print("Fitness avg. of this generation: " + str(fitness_avg) + "\n")


def show_pop_knapsack():
    print('-----------------------')
    fitness_avg = 0
    for p in pop:
        print(p + ' ' + str(determine_fitness_knapsack(p)))
        fitness_avg += average_fitness(p)
    print("Fitness avg. of this generation: " + str(fitness_avg) + "\n")


def mutation(gene):
    m = ''
    for index in range(len(gene)):
        if random() < mutation_rate:
            if gene[index] == '0':
                m += '1'
            else:
                m += '0'
        else:
            m += gene[index]
    return m


# Function that combines genetic information of two parents' chromosomes
# to generate new offspring. One-point crossovers pick two parent chromosomes
# and select a crossover point. It will swap the genetic information to the right
# of that crossover point, between the parents' chromosomes. As a result,
# I get two offspring which contain some genetic information from their parents.
def one_point_crossover(parent_1, parent_2):
    # Converting str to list
    parent_1, parent_2 = list(pop[parent_1]), list(pop[parent_2])

    crossover_point = randint(0, 10)
    # print("Crossover point :", crossover_point)

    for i in range(crossover_point, len(parent_1)):
        # Swapping the genetic information
        parent_1[i], parent_2[i] = parent_2[i], parent_1[i]

    # Convert list to str
    parent_1, parent_2 = ''.join(parent_1), ''.join(parent_2)
    # print("child_1:", parent_1)
    # print("child_2:", parent_2 + "\n\n")
    return parent_1, parent_2


def tournament_selection(inverse):
    index = 0
    fitness_max = -10000000000
    fitness_min = +11111111111

    for counter in range(tournament_selection_num):
        index_i = randint(0, len(pop) - 1)
        fitness_i = determine_fitness_ones_zeroes(pop[index_i])
        if not inverse:
            if fitness_i > fitness_max:
                fitness_max = fitness_i
                index = index_i
        else:
            if fitness_i < fitness_min:
                fitness_min = fitness_i
                index = index_i

    return index


def start_generations():
    for generation in range(n_gen):
        for individual in range(pop_size):

            # print("individual " + str(individual) + ":", pop[individual])

            index_individual_1 = tournament_selection(False)
            index_individual_2 = tournament_selection(False)

            offspring_1, offspring_2 = one_point_crossover(index_individual_1,
                                                           index_individual_2)

            offspring_1 = mutation(offspring_1)
            offspring_2 = mutation(offspring_2)

            tmp1 = None
            tmp2 = None

            while tmp1 == tmp2:
                tmp1 = tournament_selection(True)
                tmp2 = tournament_selection(True)

            pop[tmp1] = offspring_1
            pop[tmp2] = offspring_2

        print('Generation ' + str(generation))
        show_pop()


# ------------------------------------------------------------------------------


if __name__ == "__main__":
    ## USAGE for the 'Maximise the ones' problem. ------------------------------
    create_pop()
    print("Initial Population")
    show_pop()
    start_generations()
    ## -------------------------------------------------------------------------

    ## USAGE for the Knapsack problem. -----------------------------------------
    # create_pop_knapsack()
    # print("Initial Population")
    # show_pop_knapsack()
    # start_generations()
    ## -------------------------------------------------------------------------
