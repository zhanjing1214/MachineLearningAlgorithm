#This scripts is based on nb.py which additionally takes a parameter N
#and excludes the N most frequent words from its vocabulary before training the classifier.

#author: Jing Zhan

from collections import OrderedDict
import math
import sys


def step1(f1, i):
    f = open(f1, "r")
    line = f.readline()
    dic_con = {}
    dic_lib = {}
    set_voc = set()
    doc_con = 0
    doc_lib = 0
    n_con = 0
    n_lib = 0
    set_stop = remove_top_words(f1, i)
    while line != "":
        text = open(line.rstrip(), "r")
        word = text.readline().rstrip().lower()
        if line.startswith("con"):
            doc_con += 1
            while word != "":
                if word not in set_stop:
                    n_con += 1
                    insert_dict(word, dic_con)
                    set_voc.add(word)
                word = text.readline().rstrip().lower()
        else:
            doc_lib += 1
            while word != "":
                if word not in set_stop:
                    n_lib += 1
                    insert_dict(word, dic_lib)
                    set_voc.add(word)
                word = text.readline().rstrip().lower()
        line = f.readline()
    prob_con = {}
    prob_lib = {}
    for key in set_voc:
        n_con_k = dic_con.get(key, 0)
        n_lib_k = dic_lib.get(key, 0)
        prob_con[key] = (n_con_k + 1.0)/(n_con + len(set_voc))
        prob_lib[key] = (n_lib_k + 1.0)/(n_lib + len(set_voc))
    prior_con = (n_con + 0.0)/(n_con + n_lib)
    prior_lib = (n_lib + 0.0)/(n_con + n_lib)
    return prior_con, prior_lib, prob_con, prob_lib


def classify_nb(f1, f2, i):
    prior_con, prior_lib, prob_con, prob_lib = step1(f1, i)
    f = open(f2, "r")
    line = f.readline()
    right = 0
    all = 0
    while line != "":
        text = open(line.rstrip(), "r")
        all += 1
        word = text.readline().rstrip().lower()
        v_con = math.log10(prior_con)
        v_lib = math.log10(prior_lib)
        while word != "":
            if prob_con.get(word, -1) == -1:
                pass
            else:
                v_con += find_dict(word, prob_con)
                v_lib += find_dict(word, prob_lib)
            word = text.readline().rstrip().lower()
        predict = ''
        if v_con > v_lib:
            predict += "C"
            print("C")
        else:
            predict += "L"
            print("L")
        if (line.startswith("con") and predict == "C") or (line.startswith("lib") and predict == "L"):
            right += 1
        line = f.readline()
    accuracy = (right+0.0)/all;
    print("Accuracy: " + format(accuracy, ".04f"))


def find_dict(word, dic):
    result = 0
    try:
        result = math.log10(dic[word])
    except KeyError:
        pass
    return result


def remove_top_words(f1, i):
    f = open(f1, "r")
    line = f.readline()
    dic_voc = {}
    while line != "":
        text = open(line.rstrip(), "r")
        word = text.readline().rstrip().lower()
        while word != "":
            dic_voc = insert_dict(word, dic_voc)
            word = text.readline().rstrip().lower()
        line = f.readline()
    return sort_prob(dic_voc, i)


def sort_prob(prob_dic, num):
    dic_sorted = OrderedDict(sorted(prob_dic.items(), key=lambda x: x[1], reverse = True))
    i = 0
    stop_list = []
    for key in dic_sorted.keys():
        if i < num:
            stop_list.append(key)
        i += 1
    # print(set(stop_list))
    return set(stop_list)


def insert_dict(word, dic):
    try:
        dic[word] += 1
    except KeyError:
        dic[word] = 1
    return dic


#python nbStopWords.py split.train split.test 10
def main():
    classify_nb(sys.argv[1], sys.argv[2], int(sys.argv[3]))


main()
