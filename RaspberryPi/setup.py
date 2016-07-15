from setuptools import setup, find_packages
from codecs import open
from os import path

here = path.abspath(path.dirname(__file__))

with open(path.join(here, 'README.rst'), encoding='utf-8') as f:
    long_description = f.read()

setup(
    name='ADDPIO',

    version='1.0.3b1',

    description='Android IO project',
    long_description=long_description,

    #url='https://github.com/pypa/sampleproject',

    author='Sipenlatt',
    author_email='sipenlatt@gmail.com',

    license='MIT',

    classifiers=[
        'Development Status :: 4 - Beta',

        # Indicate who your project is intended for
        'Intended Audience :: Developers',
        'Topic :: Software Development :: Build Tools',

        'License :: OSI Approved :: MIT License',

        'Programming Language :: Python :: 2',
        'Programming Language :: Python :: 2.6',
        'Programming Language :: Python :: 2.7',
        'Programming Language :: Python :: 3',
        'Programming Language :: Python :: 3.3',
        'Programming Language :: Python :: 3.4',
        'Programming Language :: Python :: 3.5',
    ],

    keywords='GPIO Android Raspberry Pi sensors IO',

    packages=find_packages(exclude=['contrib', 'docs', 'tests']),

    py_modules=['ADDPIO'],
)
