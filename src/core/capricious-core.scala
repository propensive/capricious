/*
    Capricious, version [unreleased]. Copyright 2024 Jon Pretty, Propensive OÜ.

    The primary distribution site is: https://propensive.com/

    Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
    file except in compliance with the License. You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software distributed under the
    License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
    either express or implied. See the License for the specific language governing permissions
    and limitations under the License.
*/

package capricious

import hypotenuse.*
import rudiments.*

import scala.util as su

import java.util as ju
import java.security as js

import language.experimental.genericNumberLiterals

package randomNumberGenerators:
  given RandomNumberGenerator as unseeded = () => su.Random(java.util.Random())
  given RandomNumberGenerator as secureUnseeded = () => su.Random(js.SecureRandom())

  given RandomNumberGenerator as stronglySecure = () =>
    su.Random(js.SecureRandom.getInstanceStrong().nn)

  given (using seed: Seed) => RandomNumberGenerator as seeded = () =>
    su.Random(ju.Random(seed.long))

  given (using seed: Seed) => RandomNumberGenerator as secureSeeded = () =>
    su.Random(js.SecureRandom(seed.value.to(Array)))

def stochastic[ResultType](using generator: RandomNumberGenerator)(block: Random ?=> ResultType)
        : ResultType =
  block(using new Random(generator.make()))

def arbitrary[ValueType: Randomizable]()(using Random): ValueType = ValueType()

def random[ValueType: Randomizable](): ValueType =
  given Random = Random.global
  ValueType()

package randomDistributions:
  given Distribution as gaussian = Gaussian()
  given Distribution as uniformUnitInterval = UniformDistribution(0, 1)
  given Distribution as uniformSymmetricUnitInterval = UniformDistribution(-1, 1)
  given Distribution as binary = random => Double(random.long())