#this scripts takes a training dataset as input
#and print out the top 20 words with the highest probabilities (sorted) in the two categories using smoothing

# Jing Zhan

from collections import OrderedDict
import sys

def step1(f1):
    f = open(f1, "r")
    line = f.readline()
    dic_con = {}
    dic_lib = {}
    dic_voc = set()
    doc_con = 0
    doc_lib = 0
    n_con = 0
    n_lib = 0
    while line != "":
        text = open(line.rstrip(), "r")
        word = text.readline().rstrip().lower()
        if line.startswith("con"):
            doc_con += 1
            while word != "":
                n_con += 1
                dic_con = insert_dict(word, dic_con)
                dic_voc.add(word)
                word = text.readline().rstrip().lower()
        else:
            doc_lib += 1
            while word != "":
                n_lib += 1
                dic_lib = insert_dict(word, dic_lib)
                dic_voc.add(word)
                word = text.readline().rstrip().lower()
        line = f.readline()
    prob_con = {}
    prob_lib = {}
    for key in dic_voc:
        n_con_k = dic_con.get(key, 0)
        n_lib_k = dic_lib.get(key, 0)
        prob_con[key] = (n_con_k + 1.0)/(n_con + len(dic_voc))
        prob_lib[key] = (n_lib_k + 1.0)/(n_lib + len(dic_voc))
    return prob_con, prob_lib


def sort_prob(prob_dic):
    dic_sorted = OrderedDict(sorted(prob_dic.items(), key=lambda x: x[1], reverse = True))
    i = 0
    for key in dic_sorted.keys():
        if i < 20:
            print(key + " " + str(format(dic_sorted[key], ".04f")))
        i += 1


def insert_dict(word, dic):
    try:
        dic[word] += 1
    except KeyError:
        dic[word] = 1
    return dic

#python topwords.py split.train
def main():
    prob_con, prob_lib = step1(sys.argv[1])
    sort_prob(prob_lib)
    print("")
    sort_prob(prob_con)


main()
