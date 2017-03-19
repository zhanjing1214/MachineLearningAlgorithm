#Implement the Naive Bayes algorithm, using smoothing
#It will take a set of labeled training examples in split.train and a set of test examples split.test,
#and classify them using a Naive Bayes classifier.
#It outputs predicted labels for the test data, one per line,
#in the order they are listed in split.test,
#and calculate the accuracy on the test dataset.

# author:Jing Zhan
import math
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
    prior_con = (n_con + 0.0)/(n_con + n_lib)
    # print(len(prob_con))
    prior_lib = (n_lib + 0.0)/(n_con + n_lib)
    # print(len(prob_lib))
    return prior_con, prior_lib, prob_con, prob_lib


def classify_nb(f1, f2):
    prior_con, prior_lib, prob_con, prob_lib = step1(f1)
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


def insert_dict(word, dic):
    try:
        dic[word] += 1
    except KeyError:
        dic[word] = 1
    return dic

#python nb.py split.train split.test
def main():
    classify_nb(sys.argv[1], sys.argv[2])


main()
