from setuptools import setup, find_packages

with open("../../README.md", "r") as fh:
    long_description = fh.read()

with open("../../LICENSE", "r") as fh:
    license = fh.read()

setup(
    name='gvgai-gym',
    version="0.1.0",
    author_email="chrisbam4d@gmail.com",
    description="GVGAI Gym Python",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://gitlab.com/chrisbam4d/moderage-python-client",
    packages=find_packages(),
    license=license,
    install_requires = [
        "gym==0.12.5",
        "flatbuffers==1.11",
        "numpy==1.16.4"
    ]
)
