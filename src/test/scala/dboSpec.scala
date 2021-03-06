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
package com.osinka.subset

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import com.mongodb.DBObject

class dboSpec extends FunSpec with ShouldMatchers with MongoMatchers with Routines {
  describe("empty builder") {
    it("returns empty DBObject") {
      DBO.empty.builder should be('empty)
      DBO.empty.apply().keySet should be('empty)
    }
  }
  describe("DBObjectBuilder") {
    it("is BsonWritable") {
      val dbo = DBO( "a" -> DBO("b" -> 12) ).apply()
      dbo should containField("a")
      dbo.get("a").asInstanceOf[DBObject] should containKeyValue("b" -> 12)
    }
    it("has append") {
      DBO("a" -> 12, "b" -> "str").append("c" -> 14).apply() should (
        containField("a") and containField("b") and containField("c") )
    }
    it("does nothing on None") {
      // TODO: how to get rid of this ugly None:Option[Int] ?
      DBO("a" -> 12).append("a" -> (None:Option[Int])).apply() should containKeyValue("a" -> 12)
    }
    it("overrides on Some") {
      DBO("a" -> 12).append("a" -> Some("str")).apply() should containKeyValue("a" -> "str")
    }
    it("is immutable") {
      val b = DBO("a" -> 'f)
      b('f -> 12) should containKeyValue("a" -> 12)
      b('f -> 13) should containKeyValue("a" -> 13)
    }
  }
  describe("binding") {
    it("binds keys") {
      DBO("a" -> 'a).on('a -> "value") should containKeyValue("a" -> "value")
      DBO("a" -> 'a).on("a" -> "value") should containKeyValue("a" -> "value")
    }
    it("binds many keys") {
      DBO("a" -> 'a, "b" -> 'b).on('a -> 12, 'b -> false) should (
        containField("a") and containField("b") )
    }
    it("binds Some") {
      DBO("a" -> 'a).on('a -> Some("value")) should containKeyValue("a" -> "value")
    }
    it("drops None") {
      DBO("a" -> 'a).on('a -> (None:Option[Int])) should be('empty)
    }
    it("walks into subdocuments") {
      val dbo = DBO("a" -> DBO("b" -> 'key)) ('key -> 13)
      dbo should containField("a")
      dbo.get("a").asInstanceOf[DBObject] should containKeyValue("b" -> 13)
    }
  }
}
