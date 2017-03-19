#this script implements the forward algorithm.
#It treats each elements as a separate observed sequence and compute its forward table.
#The output is a list of log-probability values to stdout.
#python forward.py <dev> <hmm-trans> <hmm-emit> <hmm-prior>

#author: Jing Zhan

from numpy import *
from logsum import log_sum
import math
import sys


def read_prior(file_name):
    prior_file = open(file_name, mode="r")
    prior_vec = []
    for line in prior_file:
        prior_vec.append(float(line.rstrip().split(" ")[1]))
    prior_file.close()
    n = len(prior_vec)
    prior_vec = array(prior_vec)
    return n, prior_vec


def read_trans(file_name, n):
    trans_file = open(file_name, mode="r")
    trans_mat = zeros((n, n))
    states = []
    index = 0
    for line in trans_file:
        temp_list = line.rstrip().split(" ")
        states.append(temp_list[0])
        for i in range(n):
            trans_mat[index, i] = float(temp_list[i+1].split(":")[1])
        index += 1
    trans_file.close()
    # print trans_mat
    return states, trans_mat


def read_emit(file_name, n):
    emit_file = open(file_name, mode="r")
    first_line = emit_file.readline()
    temp_words = first_line.rstrip().split(" ")[1:]
    words = []
    for word in temp_words:
        words.append(word.split(":")[0])
    y = len(words)
    emit_mat = zeros((n, y))
    index = 0
    emit_file.close()
    emit_file = open(file_name, mode="r")
    for line in emit_file:
        temp_line = line.rstrip().split(" ")[1:]
        for i in range(y):
            emit_mat[index, i] = float(temp_line[i].split(":")[1])
        index += 1
    emit_file.close()
    return words, emit_mat


def forward_all(file_name, states, words, prior_vec, trans_mat, emit_mat):
    dev_file = open(file_name, mode="r")
    for line in dev_file:
        sentence = line.rstrip().split(" ")
        forward_each(sentence, states, words, prior_vec, trans_mat, emit_mat)
    dev_file.close()


def forward_each(sentence, states, words, prior_vec, trans_mat, emit_mat):
    o1 = sentence[0]
    afa = []
    for i in range(len(states)):
        index = find_b(o1, words)
        ini = math.log(prior_vec[i]) + math.log(emit_mat[i, index])
        afa.append(ini)
    for t in range(0, len(sentence)-1):
        temp_afa = afa[:]
        for i in range(len(states)):
            b_j = find_b(sentence[t+1], words)
            temp_list = []
            for j in range(len(afa)):
                temp_list.append(temp_afa[j] + math.log(trans_mat[j, i]))
            afa[i] = log_sum_all(temp_list) + math.log(emit_mat[i, b_j])
    temp = afa[0]
    final_p = afa[:]
    for i in range(1, len(afa)):
        temp = log_sum(temp, final_p[i])
    print temp


def log_sum_all(n_list):
    temp = n_list[0]
    for i in range(1, len(n_list)):
        temp = log_sum(temp, n_list[i])
    return temp


def find_b(word, words):
    for j in range(len(words)):
        if words[j] == word:
            return j
    return -1


def main():
    n, prior_vec = read_prior(sys.argv[4])
    states, trans_mat = read_trans(sys.argv[2], n)
    words, emit_mat = read_emit(sys.argv[3], n)
    forward_all(sys.argv[1], states, words, prior_vec, trans_mat, emit_mat)

# def main():
#     n, prior_vec = read_prior('hmm-prior.txt')
#     states, trans_mat = read_trans('hmm-trans.txt', n)
#     words, emit_mat = read_emit('hmm-emit.txt', n)
#     forward_all('dev.txt', states, words, prior_vec, trans_mat, emit_mat)


main()
