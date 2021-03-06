/**
 * Copyright (C) 2013 Alexander Azarov <azarov@osinka.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.osinka

package object subset {
  import scala.language.implicitConversions

  implicit def kvFromStrKey[A](t: (String,A))(implicit writer: BsonWritable[A]) =
    DBO.KV(t._1, writer(t._2))

  implicit def kvFromSymKey[A](t: (Symbol,A))(implicit writer: BsonWritable[A]) =
    DBO.KV(t._1.name, writer(t._2))
}